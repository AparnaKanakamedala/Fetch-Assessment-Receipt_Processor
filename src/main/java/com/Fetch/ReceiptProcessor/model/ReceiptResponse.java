package com.Fetch.ReceiptProcessor.model;

import java.util.UUID;

public class ReceiptResponse {
    private UUID id;

    // Constructor
    public ReceiptResponse(UUID id, Reciept reciept) {

        this.id = id;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
