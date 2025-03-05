package com.mog.authserver.company.controller;

import com.mog.authserver.common.response.BaseResponseBody;
import com.mog.authserver.common.status.enums.SuccessStatus;
import com.mog.authserver.company.dto.response.CompanyInfoResponseDTO;
import com.mog.authserver.company.dto.response.CompanyPassDTO;
import com.mog.authserver.company.service.CompanyQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyQueryController {

    private final CompanyQueryService companyQueryService;

    @GetMapping("/infos")
    public ResponseEntity<BaseResponseBody<Page<CompanyInfoResponseDTO>>> getCompanyInfosPaging(
            @RequestParam(name = "page") Integer page, @RequestParam(name = "size") Integer size) {
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(companyQueryService.getCompanyInfoPaging(page, size)));
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<BaseResponseBody<CompanyInfoResponseDTO>> getCompanyInfo(
            @PathVariable(name = "id") Long id) {
        CompanyInfoResponseDTO companyInfo = companyQueryService.getCompanyInfo(id);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(companyInfo));
    }

    @GetMapping("/pass/{id}")
    public ResponseEntity<BaseResponseBody<CompanyPassDTO>> getCompanyPass(
            @PathVariable(name = "id") Long id) {
        CompanyPassDTO companyPass = companyQueryService.getCompanyPass(id);
        return ResponseEntity.status(SuccessStatus.OK.getHttpStatus())
                .body(SuccessStatus.OK.getBaseResponseBody(companyPass));
    }
}
