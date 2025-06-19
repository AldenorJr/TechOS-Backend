package br.com.tech.os.ostech.model.dto.clientDTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ClientAnalyticsResponse {

    private Integer totalClients;
    private Integer lastMonthClients;
    private Integer lastWeekClients;
    private Integer last24HoursClients;

}
