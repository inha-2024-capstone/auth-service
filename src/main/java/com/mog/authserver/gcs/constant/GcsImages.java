package com.mog.authserver.gcs.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GcsImages {
    @Value("${gcp.company-image}")
    public String DEFAULT_COMPANY_IMAGE = "https://storage.googleapis.com/yoger-gcs/company.png";
    @Value("${gcp.user-image}")
    public String DEFAULT_USER_IMAGE = "https://storage.googleapis.com/yoger-gcs/user.png";

}
