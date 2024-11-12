package com.Fetch.ReceiptProcessor;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.Fetch.ReceiptProcessor.model.PointsResponse;
import com.Fetch.ReceiptProcessor.model.ReceiptResponse;
import com.Fetch.ReceiptProcessor.model.Reciept;
import com.Fetch.ReceiptProcessor.service.FetchService;

@RestController
@RequestMapping("/reciepts")
public class FetchController {

    @Autowired
    FetchService fetchService;

    @PostMapping("/process")
    public ReceiptResponse processReciept(@RequestBody Reciept reciept) {
        UUID id = fetchService.processReciept(reciept);

        return new ReceiptResponse(id, reciept);
    }

    @GetMapping("/{id}/points")
    public PointsResponse getPointsForRecepiptWithId(@PathVariable("id") UUID id) {
        int points = fetchService.getPointsForReceiptId(id);

        return new PointsResponse(points);
    }
}
