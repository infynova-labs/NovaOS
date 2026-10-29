/**
 * Copyright (C) 2026 InfYNova NovaOS
 */

package com.infynova.novaid;

interface INovaAIStreamCallback {
    void onToken(String token);
    void onComplete();
    void onError(String error);
}
