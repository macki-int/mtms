package pl.trollsystems.mtms.service;

import org.junit.jupiter.api.Test;
import pl.trollsystems.mtms.model.RawReadout;
import pl.trollsystems.mtms.model.Readout;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ReadoutParserTest {

    @Test
    public void shouldParseByMainFactor() {
        //given
        RawReadout rawReadout = new RawReadout();
        rawReadout.setId(1L);
        rawReadout.setFileName("000000_00 A.txt");
        rawReadout.setReading("#F/a=0#T/s=704635259/p=22.04.30,14:00:40+08/m=704635201#M/b=+0.1060674+1.0981121+11.835693+0.9920499+1.5700000+0.1059414+1.0981221+11.851806+0.9921699+3.5699999+0.1057745+1.0981055+11.851806+0.9923299+5.9699997+0.1057929+1.0981798+11.851806+0.9923899+7.7600002+0.1056595+1.0981359+11.867431+0.9924799+9.7100000+0.1058231+1.0980808+11.867431+0.9922599+11.659999+0.1056474+1.0980266+11.851806+0.9923800+13.020000+0.1057816+1.0980154+11.835693+0.9922400+14.000000+0.1057512+1.0980001+11.867431+0.9922500+15.130000+0.1058900+1.0978530+11.867431+0.9919599+16.149999+0.1058675+1.0976502+11.851806+0.9917699+17.149999+0.1060557+1.0975620+11.867431+0.9915100+18.020000+0.1060687+1.0974719+11.867431+0.9914100+18.659999+0.1062510+1.0972872+11.883544+0.9910499+18.969999+0.1063562+1.0971460+11.867431+0.9907999+19.309999+0.1063182+1.0970362+11.883544+0.9907199+19.340000/c=+1+1#I/n=656/s=31/b=99/e=9.20/f=21.15/h=35/v=+3.918#G/a=Measurement#E/e#X/a=3010");
        rawReadout.setTransmisionDateTime("2022-04-30 18:42:55");
        rawReadout.setRawImport(false);

        List<RawReadout> rawReadoutList = new ArrayList<>();
        rawReadoutList.add(rawReadout);

        ReadoutParser readoutParser = new ReadoutParser();

        //when
        List<Readout> readoutList = readoutParser.parseByFactor(rawReadoutList);

        //then
        for (Readout r : readoutList) {
            System.out.println(r.getDescription());

            assertEquals("22.04.30,14:00:40+08", r.getTransmitterParameterReading().getStartMeasureDataTime());
            assertEquals("656", r.getTransmitterParameterReading().getSerialNumber());
            assertEquals("31", r.getTransmitterParameterReading().getSignalLevel());
            assertEquals("99", r.getTransmitterParameterReading().getBatteryCapacity());
            assertEquals("35", r.getTransmitterParameterReading().getHumidity());
            assertEquals("+3.918", r.getTransmitterParameterReading().getBatteryVoltage());

        }

    }
}