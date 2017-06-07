package com.wasp.gateway.enquiry.balance.service

import com.paymentcomponents.common.models.Bank
import com.paymentcomponents.common.request.BalanceEnquiryRequest
import com.paymentcomponents.common.response.BalanceEnquiryResponse
import com.wasp.gateway.enquiry.balance.ERROR_CODES
import com.wasp.gateway.enquiry.balance.exceptions.WaspApiValidationException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

/**
 * Created by aodunlami on 5/19/17.
 */
@Service
class BalanceEnquiryService {

    private RestTemplate restTemplate

    @Autowired
    BalanceEnquiryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate
    }


    public BalanceEnquiryResponse initiateBalanceEnquiry(BalanceEnquiryRequest balanceEnquiryRequest, String context) {

        validationChecks(balanceEnquiryRequest)

        Bank instructedBank = getBankByBankCode(balanceEnquiryRequest.instructedInstitutionCode)

        ////restTemplate.exchange("http://balance-enquiry-service" + context, HttpMethod.POST, new HttpEntity<BalanceEnquiryRequest>(balanceEnquiryRequest), new ParameterizedTypeReference<ResponseEntity>() {})

        BalanceEnquiryResponse response = restTemplate.exchange(instructedBank.waspConnectUrl + context, HttpMethod.POST, new HttpEntity<BalanceEnquiryRequest>(balanceEnquiryRequest), new ParameterizedTypeReference<BalanceEnquiryResponse>() {})

        return response
    }

    public static void validationChecks(BalanceEnquiryRequest balanceEnquiryRequest) {

        // ---------- value validations ----------
        if (!balanceEnquiryRequest.targetAccountNumber || balanceEnquiryRequest.targetAccountNumber.is("")) {
            throw new WaspApiValidationException(ERROR_CODES.constraint_violation.toString(), "Target account must be specified.")
        }
        if (!balanceEnquiryRequest.instructedInstitutionCode || balanceEnquiryRequest.instructedInstitutionCode.is("")) {
            throw new WaspApiValidationException(ERROR_CODES.constraint_violation.toString(), "Target Institution must be specified.")
        }

        // ---------- logical validations ----------

    }

    private Bank getBankByBankCode(String bankCode) {
        try {
            Bank bank = restTemplate.exchange("http://application-service/banks/search/by-central-bank-code?centralBankCode={bankCode}", HttpMethod.GET, new HttpEntity<Object>(null), new ParameterizedTypeReference<Bank>() {
            }, bankCode)?.body
            if (!bank.waspConnectUrl || bank.waspConnectUrl.isEmpty()) {
                throw new WaspApiValidationException(ERROR_CODES.instructed_bank_url_not_found.toString(), "Instructed bank $bankCode endpoint not found")
            }
            return bank
        } catch (Exception e) {
            throw new WaspApiValidationException(ERROR_CODES.instructed_bank_not_found.toString(), "Instructed bank $bankCode not found")
        }
    }
}
