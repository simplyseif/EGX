package org.egx.stocks.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.egx.stocks.entity.OHCLVStatistics;
import org.egx.stocks.services.HistoricalStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/historical-stocks")
@Tag(name = "Historical Stocks Controller", description = "APIs for retrieving historical stock data")
public class HistoricalStocksController {

    @Autowired
    private HistoricalStockService historicalStockService;
    /*
    preferred parameters and precision
    1 day 5 minutes period
    1 week 30 minutes period
    1 month 1 day period
    * */
    @Operation(
            summary = "Get OHCLV statistics for a stock",
            description = "Get OHCLV (Open, High, Close, Low, Volume) statistics for a stock based on specified parameters"
    )
    @GetMapping("/{reutersCode}")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class)))
    @ApiResponse(responseCode = "404", description = "Stock/Equity not found")
    Page<OHCLVStatistics> getOHCLVStatistics(
            @PathVariable("reutersCode") String reutersCode,
            @RequestParam(defaultValue = "30 minutes") String periodParam,
            @RequestParam(defaultValue = "1 weeks") String intervalParam,
            @RequestParam(defaultValue = "0" ) int page,
            @RequestParam(defaultValue = "10") int size){
        return historicalStockService.getOHCLVStatistics(reutersCode, periodParam, intervalParam, size, page);
    }
}
