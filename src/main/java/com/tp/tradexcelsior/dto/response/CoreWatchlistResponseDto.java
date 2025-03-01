package com.tp.tradexcelsior.dto.response;

import com.tp.tradexcelsior.entity.MarketTrendByMonth;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class CoreWatchlistResponseDto {

  private String id;
  private String company;
  private String analysisLink;
  private String Sector;
  private String marketCap;
  private List<MarketTrendByMonth> marketTrendByMonthList;

}
