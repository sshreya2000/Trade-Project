package com.tp.tradexcelsior.service.admin;

import com.tp.tradexcelsior.dto.request.CoreWatchlistRequestDto;
import com.tp.tradexcelsior.dto.request.WatchlistTrendUpdateDto;
import com.tp.tradexcelsior.dto.response.CoreWatchlistResponseDto;
import com.tp.tradexcelsior.dto.response.PagedResponse;
import java.util.List;

public interface IAdminCoreWatchlistService {
  CoreWatchlistResponseDto addCoreWatchlist(CoreWatchlistRequestDto watchlistRequestDto);
  CoreWatchlistResponseDto getCoreWatchList(String coreWatchlistId);
  PagedResponse<CoreWatchlistResponseDto> getAllCoreWatchlist(int page, int size, String monthYear, String sortBy, String sortDirection);
  CoreWatchlistResponseDto updateCoreWatchlist(CoreWatchlistRequestDto watchlistRequestDto, String coreWatchlistId);
  PagedResponse<CoreWatchlistResponseDto> searchCoreWatchlists(String company, int page, int size);
  CoreWatchlistResponseDto updateCurrentMonthTrend(WatchlistTrendUpdateDto watchlistTrendUpdateDto);
  void deleteCoreWatchList(String coreWatchlistId);
}
