package io.mosip.print.kernal.biometrics.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RequestDto {
    private String version;
    private String request;
}
