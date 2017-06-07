package com.wasp.gateway.enquiry.balance.controller

import com.paymentcomponents.common.request.BalanceEnquiryRequest
import com.paymentcomponents.common.response.BalanceEnquiryResponse
import com.wasp.gateway.enquiry.balance.service.BalanceEnquiryService
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.RestTemplate

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by aodunlami on 5/19/17.
 */
@RestController
@RequestMapping("/v1/account/balanceenquiry")
class BalanceEnquiryController {

    private RestTemplate restTemplate
    private BalanceEnquiryService balanceEnquiryService

    @Autowired
    BalanceEnquiryController(RestTemplate restTemplate, BalanceEnquiryService balanceEnquiryService) {
        this.restTemplate = restTemplate
        this.balanceEnquiryService = balanceEnquiryService
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "initiateBalanceEnquiry", notes = "Balance enquiry initiation")
    @ApiResponses([
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad Request", response = Error.class),
            @ApiResponse(code = 401, message = "Unauthorized", response = Error.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = Error.class)
    ])
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody BalanceEnquiryResponse initiateBalanceEnquiry(
            @RequestBody BalanceEnquiryRequest balanceEnquiryRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return balanceEnquiryService.initiateBalanceEnquiry(balanceEnquiryRequest, httpServletRequest.servletPath)
    }
}
