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

package com.avispa.ecm.util.json;

import com.avispa.ecm.util.exception.EcmException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(new Locale("pl","PL"));
        decimalFormat.setParseBigDecimal(true);

        try {
            BigDecimal bd = (BigDecimal) decimalFormat.parse(jp.getValueAsString());
            bd = bd.setScale(2, RoundingMode.HALF_UP);

            return bd;
        } catch (ParseException e) {
            throw new EcmException("Can't deserialize " + jp.getValueAsString() + " value as money", e);
        }
    }    
}