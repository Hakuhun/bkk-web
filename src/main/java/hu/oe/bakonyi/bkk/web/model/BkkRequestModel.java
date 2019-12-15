package hu.oe.bakonyi.bkk.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BkkRequestModel {
    private Location location;
    private RouteInfo routeInfo;
    private LocalDateTime time;
}
