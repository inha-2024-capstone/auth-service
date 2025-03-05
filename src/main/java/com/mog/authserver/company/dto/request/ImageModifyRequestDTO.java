package com.mog.authserver.company.dto.request;

import com.mog.authserver.gcs.validator.ValidImage;
import org.springframework.web.multipart.MultipartFile;

public record ImageModifyRequestDTO(@ValidImage MultipartFile image) {
}
