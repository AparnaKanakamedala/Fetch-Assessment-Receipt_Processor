package com.Fetch.ReceiptProcessor.repositories;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

//PointsRepository act as an in-memory storage solution, 
// making it suitable for temporary data storage and testing.

@Repository
public class PointsRepository {

    private Map<UUID, Integer> receiptUuidToPoints = new ConcurrentHashMap<>();

    public void addReceiptIdAndPoints(UUID receiptUuid, int points) {
        receiptUuidToPoints.put(receiptUuid, points);
    }

    public Integer getPointsForReceiptUuid(UUID receiptUuid) {
        return receiptUuidToPoints.getOrDefault(receiptUuid, null);
    }
}
