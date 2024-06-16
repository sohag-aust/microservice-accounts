package com.ezybytes.account.dto;

import lombok.Builder;

@Builder
public record AccountsContactInfoDtoWithBuildInfo(
        AccountsContactInfoDto accountsContactInfoDto,
        String buildInfo
) { }
