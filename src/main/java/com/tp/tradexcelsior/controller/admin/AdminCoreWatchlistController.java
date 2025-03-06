package com.tp.tradexcelsior.controller.admin;

import com.tp.tradexcelsior.dto.request.CoreWatchlistRequestDto;
import com.tp.tradexcelsior.dto.request.WatchlistTrendUpdateDto;
import com.tp.tradexcelsior.dto.response.CoreWatchlistResponseDto;
import com.tp.tradexcelsior.dto.response.CommonResponse;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import com.tp.tradexcelsior.service.admin.impl.AdminCoreWatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/core-watchlist")
public class AdminCoreWatchlistController {

  @Autowired
  private AdminCoreWatchlistService adminCoreWatchlistService;

  // Create a new core watchlist
  @Operation(summary = "Create a new core watchlist", description = "Create a new core watchlist entry for a company")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Core watchlist successfully created",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoreWatchlistResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Validation failed",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PostMapping
  public ResponseEntity<CoreWatchlistResponseDto> addCoreWatchlist(@RequestBody @Valid CoreWatchlistRequestDto watchlistRequestDto) {
    CoreWatchlistResponseDto createdWatchlist = adminCoreWatchlistService.addCoreWatchlist(watchlistRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdWatchlist);
  }

  // Get a core watchlist by ID
  @Operation(summary = "Get a core watchlist by ID", description = "Fetch the details of a core watchlist by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Core watchlist details fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoreWatchlistResponseDto.class))),
      @ApiResponse(responseCode = "404", description = "Core watchlist not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<CoreWatchlistResponseDto> getCoreWatchlist(@PathVariable String id) {
    CoreWatchlistResponseDto watchlistResponse = adminCoreWatchlistService.getCoreWatchList(id);
    return ResponseEntity.ok(watchlistResponse);  // Return status 200 with core watchlist details
  }

  // Get a paginated list of core watchlists
  @Operation(summary = "Get a paginated list of core watchlists", description = "Retrieve a paginated list of core watchlists")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "List of core watchlists fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
  })
  @GetMapping
  public ResponseEntity<PagedResponse<CoreWatchlistResponseDto>> getAllCoreWatchlists(
      @RequestParam(defaultValue = "0") int page,  // Default to the first page
      @RequestParam(defaultValue = "10") int size,   // Default to a page size of 10
      @RequestParam(required = false) String monthYear,  // Optional monthYear for sorting by a specific month
      @RequestParam(defaultValue = "asc") String sortDirection, // Default sorting direction "asc"
      @RequestParam(defaultValue = "company") String sortBy  // Default sorting by company
  ) {
    PagedResponse<CoreWatchlistResponseDto> pagedResponse = adminCoreWatchlistService.getAllCoreWatchlist(
        page, size, monthYear, sortDirection, sortBy
    );
    return ResponseEntity.ok(pagedResponse);  // Return status 200 with paginated and sorted core watchlist list
  }


  // Update an existing core watchlist
  @Operation(summary = "Update an existing core watchlist", description = "Update the details of a core watchlist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Core watchlist successfully updated",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoreWatchlistResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid data provided",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "404", description = "Core watchlist not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<CoreWatchlistResponseDto> updateCoreWatchlist(
      @RequestBody @Valid CoreWatchlistRequestDto watchlistRequestDto, @PathVariable String id) {
    CoreWatchlistResponseDto updatedWatchlist = adminCoreWatchlistService.updateCoreWatchlist(watchlistRequestDto, id);
    return ResponseEntity.ok(updatedWatchlist);  // Return status 200 with updated core watchlist
  }

  // Search core watchlists by parameters
  @Operation(summary = "Search core watchlists", description = "Search core watchlists based on company name or other parameters")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Search results fetched successfully",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = PagedResponse.class)))
  })
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<CoreWatchlistResponseDto>> searchCoreWatchlists(
      @RequestParam(required = false) String company,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {

    PagedResponse<CoreWatchlistResponseDto> searchResults = adminCoreWatchlistService.searchCoreWatchlists(company, page, size);
    return ResponseEntity.ok(searchResults);  // Return status 200 with paginated search results
  }

  // Update the trend for the current month of a specific company
  @Operation(summary = "Update current month trend for a company", description = "Update the trend for the current month for a specific company's core watchlist")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Core watchlist updated successfully with the new trend",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoreWatchlistResponseDto.class))),
      @ApiResponse(responseCode = "400", description = "Invalid trend provided",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "404", description = "Core watchlist not found for the given company",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/update-trend")
  public ResponseEntity<CoreWatchlistResponseDto> updateCurrentMonthTrend(@RequestBody @Valid WatchlistTrendUpdateDto watchlistTrendUpdateDto) {

    CoreWatchlistResponseDto updatedWatchlist = adminCoreWatchlistService.updateCurrentMonthTrend(watchlistTrendUpdateDto);
    return ResponseEntity.ok(updatedWatchlist);
  }

  // Update all core watchlists for the new month
  @Operation(summary = "Update all core watchlists for the new month", description = "Update all core watchlists with the new month trend (default as empty if not provided)")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Core watchlists updated successfully for the new month",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class))),
      @ApiResponse(responseCode = "400", description = "Failed to update core watchlists",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @PutMapping("/update-all")
  public ResponseEntity<CommonResponse> updateCoreWatchlistsForNewMonth() {
    CommonResponse response = adminCoreWatchlistService.updateCoreWatchlistsForNewMonth();
    return ResponseEntity.ok(response);  // Return status 200 with response
  }


  // Delete a core watchlist by ID
  @Operation(summary = "Delete a core watchlist by ID", description = "Delete a core watchlist entry by its ID")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Core watchlist successfully deleted"),
      @ApiResponse(responseCode = "404", description = "Core watchlist not found",
          content = @Content(mediaType = "application/json", schema = @Schema(implementation = CommonResponse.class)))
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCoreWatchlist(@PathVariable String id) {
    // Call service method to delete the core watchlist
    adminCoreWatchlistService.deleteCoreWatchList(id);

    // Return 204 No Content status after successful deletion
    return ResponseEntity.noContent().build();
  }
}
