package com.tp.tradexcelsior.util;

import com.tp.tradexcelsior.dto.request.CoreWatchlistRequestDto;
import com.tp.tradexcelsior.dto.response.CoreWatchlistResponseDto;
import com.tp.tradexcelsior.entity.CoreWatchlist;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.modelmapper.ModelMapper;

public class WatchlistUtils {

  private static ModelMapper modelMapper = new ModelMapper();

  // Utility method to map CoreWatchlist to CoreWatchlistResponseDto (assuming ModelMapper or similar approach)
  public static CoreWatchlistResponseDto mapToCoreWatchlistResponseDto(CoreWatchlist coreWatchlist) {
    return modelMapper.map(coreWatchlist, CoreWatchlistResponseDto.class);
  }

  // Utility method to reverse map CoreWatchlistResponseDto to CoreWatchlist
  public static CoreWatchlist mapToCoreWatchlist(CoreWatchlistRequestDto coreWatchlistRequestDto) {
    return modelMapper.map(coreWatchlistRequestDto, CoreWatchlist.class);
  }

  public static long parseMarketCap(String marketCap) {
    // Regex to extract number and unit (B for billion, M for million)
    String regex = "([\\d.]+)([B|M])";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(marketCap);

    if (matcher.matches()) {
      double value = Double.parseDouble(matcher.group(1)); // Get the numeric part
      String unit = matcher.group(2); // Get the unit part

      if ("B".equals(unit)) {
        return (long) (value * 1_000_000_000); // Convert to billions
      } else if ("M".equals(unit)) {
        return (long) (value * 1_000_000); // Convert to millions
      }
    }
    return 0; // Return 0 if the format is incorrect
  }
}