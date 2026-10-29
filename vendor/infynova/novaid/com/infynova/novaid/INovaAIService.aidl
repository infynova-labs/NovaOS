/*
 * Copyright (C) 2026 InfYNova NovaOS
 */

package com.infynova.novaid;

import com.infynova.novaid.INovaAIStreamCallback;

interface INovaAIService {
    /**
     * Sends a prompt to the local SLM engine and receives a response.
     */
    String queryModel(String prompt, String params);

    /**
     * Streams inference results back to the caller.
     */
    void streamQuery(String prompt, INovaAIStreamCallback callback);
}
