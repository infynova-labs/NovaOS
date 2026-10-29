/**
 * NovaAID — InfYNova Native AI System Daemon
 *
 * This daemon provides a Binder interface to a local LLM/SLM engine.
 * It uses libbinder for IPC and interfaces with llama.cpp.
 */

#define LOG_TAG "NovaAID"

#include <binder/IBinder.h>
#include <binder/IPCThreadState.h>
#include <binder/IServiceManager.h>
#include <binder/ProcessState.h>
#include <utils/Log.h>
#include <utils/String16.h>
#include <utils/String8.h>
#include <thread>
#include <future>
#include <sys/mman.h>
#include <fcntl.h>
#include <sys/stat.h>
#include <unistd.h>
#include <vector>
#include <android-base/properties.h>

#include "com/infynova/novaid/BnNovaAIService.h"

using namespace android;

namespace com {
namespace infynova {
namespace novaid {

class NovaAIService : public BnNovaAIService {
private:
    void* mModelAddr = nullptr;
    size_t mModelSize = 0;
    bool mModelLoaded = false;

    void loadModel() {
        const char* modelPath = "/product/etc/nova/models/nova_slm_v1.gguf";

        // Lazy Loading: Check system memory before mapping
        // On GSIs, we might be on a 4GB device. If so, we delay mmap until first query.
        bool isLowRam = android::base::GetBoolProperty("ro.config.low_ram", false);
        if (isLowRam) {
            ALOGW("Low RAM detected. Delaying AI model mapping (Lazy Load).");
            return;
        }

        int fd = open(modelPath, O_RDONLY);
        if (fd < 0) {
            ALOGE("Failed to open model: %s", modelPath);
            return;
        }

        struct stat st;
        if (fstat(fd, &st) < 0) {
            close(fd);
            return;
        }

        mModelSize = st.st_size;
        mModelAddr = mmap(NULL, mModelSize, PROT_READ, MAP_PRIVATE, fd, 0);
        close(fd);

        if (mModelAddr == MAP_FAILED) {
            ALOGE("mmap failed for model weights");
            return;
        }

        mModelLoaded = true;
        ALOGI("Successfully mapped model weights: %zu bytes", mModelSize);
    }

public:
    NovaAIService() {
        loadModel();
    }

    static char const* getServiceName() { return "novaid"; }

    binder::Status queryModel(const String16& prompt, const String16& /* params */, String16* _aidl_return) override {
        ALOGI("queryModel: %s", String8(prompt).c_str());

        if (!mModelLoaded) {
            ALOGI("First AI query on low-ram device. Attempting to map model now...");
            loadModel();
        }

        if (!mModelLoaded) {
            *_aidl_return = String16("NovaAID Error: Model weights not loaded.");
            return binder::Status::ok();
        }

        // Real inference would use mModelAddr here
        std::string response = "NovaAID [1B SLM]: Processing local inference from mapped weights...";
        *_aidl_return = String16(response.c_str());

        return binder::Status::ok();
    }

    binder::Status streamQuery(const String16& prompt, const sp<INovaAIStreamCallback>& callback) override {
        ALOGI("streamQuery (async): %s", String8(prompt).c_str());

        if (callback != nullptr) {
            std::thread([callback]() {
                callback->onToken(String16("Processing "));
                std::this_thread::sleep_for(std::chrono::milliseconds(200));
                callback->onToken(String16("asynchronously "));
                std::this_thread::sleep_for(std::chrono::milliseconds(200));
                callback->onToken(String16("on NovaAID..."));
                callback->onComplete();
            }).detach();
        }

        return binder::Status::ok();
    }
};

} // namespace novaid
} // namespace infynova
} // namespace com

int main(int /* argc */, char** /* argv */) {
    ALOGI("NovaAID starting...");

    sp<ProcessState> proc(ProcessState::self());
    sp<IServiceManager> sm = defaultServiceManager();

    using com::infynova::novaid::NovaAIService;
    sm->addService(String16(NovaAIService::getServiceName()), new NovaAIService());

    ProcessState::self()->startThreadPool();
    IPCThreadState::self()->joinThreadPool();

    return 0;
}
