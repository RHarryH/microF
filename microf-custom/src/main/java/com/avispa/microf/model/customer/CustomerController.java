/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2023 Rafał Hiszpański
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.avispa.microf.model.customer;

import com.avispa.ecm.model.base.controller.BaseController;
import com.avispa.microf.model.customer.exception.CustomerInUseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@Slf4j
@Controller
@RequestMapping("/customer")
public class CustomerController extends BaseController<Customer, CustomerDto, CustomerService> {

    public CustomerController(CustomerService service) {
        super(service);
    }

    @Override
    @DeleteMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") UUID id) {
        try {
            super.delete(id);
        } catch(DataIntegrityViolationException e) {
            String errorMessage = String.format("Customer '%s' is in use.", getService().findById(id).getObjectName());
            log.error(errorMessage, e);
            throw new CustomerInUseException(errorMessage);
        }
    }
}
