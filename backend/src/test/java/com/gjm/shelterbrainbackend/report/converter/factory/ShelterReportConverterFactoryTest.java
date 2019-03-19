package com.gjm.shelterbrainbackend.report.converter.factory;

import com.gjm.shelterbrainbackend.report.converter.CsvShelterReportConverter;
import com.gjm.shelterbrainbackend.report.converter.PdfShelterReportConverter;
import com.gjm.shelterbrainbackend.report.converter.WebShelterReportConverter;
import org.junit.Test;

import static org.junit.Assert.*;

public class ShelterReportConverterFactoryTest {
    @Test
    public void getInstanceByFormatLegal() {
        assertEquals(new WebShelterReportConverter(), ShelterReportConverterFactory.getInstanceByFormat("web"));
        assertEquals(new PdfShelterReportConverter(), ShelterReportConverterFactory.getInstanceByFormat("pdf"));
        assertEquals(new CsvShelterReportConverter(), ShelterReportConverterFactory.getInstanceByFormat("csv"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getInstanceByFormatIllegal() {
        ShelterReportConverterFactory.getInstanceByFormat("xyz");
    }
}