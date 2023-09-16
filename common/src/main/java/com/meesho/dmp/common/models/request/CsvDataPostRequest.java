package com.meesho.dmp.common.models.request;

import com.meesho.dmp.common.dto.CsvData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CsvDataPostRequest {
    @NotEmpty
    @Valid
    private List<CsvData> csvDataList;
}
