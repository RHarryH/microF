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

package com.avispa.microf.model.invoice.service.replacer;

import com.avispa.microf.model.invoice.service.file.variable.Variable;
import org.odftoolkit.odfdom.doc.OdfTextDocument;
import org.odftoolkit.odfdom.dom.element.table.TableTableElement;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Map;

/**
 * Contains additional invoice specific implementation for handling tables
 *
 * @author Rafał Hiszpański
 */
public final class InvoiceOdtReplacer extends OdtReplacer {
    private static final String POSITIONS_TABLE = "Positions";
    private static final String VAT_MATRIX_TABLE = "VatMatrix";

    public InvoiceOdtReplacer(OdfTextDocument document) {
        super(document);
    }

    @Override
    protected void processContent(Map<String, String> variables) throws SAXException, IOException {
        super.processContent(variables);

        NodeList nodes = getContentDom().getElementsByTagName(TableTableElement.ELEMENT_NAME.getQName());
        for(int i = 0; i < nodes.getLength(); i++) {
            TableTableElement table = (TableTableElement) nodes.item(i);
            if(table.getTableNameAttribute().equals(POSITIONS_TABLE)) {
                processTable(table, variables, Variable.VP_POSITIONS, 9);
            }
            if(table.getTableNameAttribute().equals(VAT_MATRIX_TABLE)) {
                processTable(table, variables, Variable.VP_VAT_MATRIX, 4);
            }
        }
    }
}
