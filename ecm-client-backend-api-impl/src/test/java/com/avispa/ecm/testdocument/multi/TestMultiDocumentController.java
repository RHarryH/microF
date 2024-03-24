/*
 * Avispa μF - invoice generating software built on top of Avispa ECM
 * Copyright (C) 2024 Rafał Hiszpański
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

package com.avispa.ecm.testdocument.multi;

import com.avispa.ecm.model.base.controller.BaseMultiTypeEcmController;
import com.avispa.ecm.testdocument.simple.TestDocumentService;
import com.avispa.ecm.testdocument.simple.TestDocumentDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author Rafał Hiszpański
 */
@RestController
@RequestMapping("/v1/test-document-multi")
public class TestMultiDocumentController extends BaseMultiTypeEcmController<TestMultiDocument, TestMultiDocumentCommonDto, TestMultiDocumentDto, TestMultiDocumentService> {
    protected TestMultiDocumentController(TestMultiDocumentService service) {
        super(service);
    }

    @Override
    protected void add(TestMultiDocumentDto dto) {
        service.add(dto);
    }

    @Override
    protected void update(UUID id, TestMultiDocumentDto dto) {
        service.update(dto, id);
    }
}
