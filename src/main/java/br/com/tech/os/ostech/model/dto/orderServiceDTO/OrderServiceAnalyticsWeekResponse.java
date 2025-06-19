package br.com.tech.os.ostech.model.dto.orderServiceDTO;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderServiceAnalyticsWeekResponse {

    private Integer entrySunday;
    private Integer entryMonday;
    private Integer entryTuesday;
    private Integer entryWednesday;
    private Integer entryThursday;
    private Integer entryFriday;
    private Integer entrySaturday;

    private Integer exitSunday;
    private Integer exitMonday;
    private Integer exitTuesday;
    private Integer exitWednesday;
    private Integer exitThursday;
    private Integer exitFriday;
    private Integer exitSaturday;


}
