package pl.trollsystems.mtms.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.trollsystems.mtms.model.RawReadout;
import pl.trollsystems.mtms.model.Readout;
import pl.trollsystems.mtms.repository.RawReadoutRepository;
import pl.trollsystems.mtms.service.ReadoutParser;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/rawreadouts")
public class RawReadoutController {
    private RawReadoutRepository rawReadoutRepository;
    @Autowired
    private ReadoutParser readoutParser;

    @Autowired
    public RawReadoutController(RawReadoutRepository rawReadoutRepository) {
        this.rawReadoutRepository = rawReadoutRepository;
    }

    @PostMapping
    public ResponseEntity<?> add(@RequestBody String string) {

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class,
                (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {

                    try {
                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } catch (DateTimeParseException e) {
                        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"));
                    }
                }).create();
        Type listType = new TypeToken<ArrayList<RawReadout>>() {
        }.getType();
        ArrayList<RawReadout> rawReadoutList = gson.fromJson(string, listType);

        for (RawReadout rawReadout : rawReadoutList) {
            rawReadoutRepository.save(rawReadout);
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/non-imports")
    public ResponseEntity<?> findAllWithoutImport() {

        return ResponseEntity.ok().body(rawReadoutRepository.findByRawImportFalse());
    }

    //    @Scheduled(fixedDelay = 600_000)
    //    @Secured("ADMIN")
    @GetMapping("/do-imports")
    public ResponseEntity<?> makeImport() {
        List<RawReadout> rawReadouts = rawReadoutRepository.findByRawImportFalse();
        if (rawReadouts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

//        ReadoutParser readoutParser = new ReadoutParser();
        List<Readout> readouts = readoutParser.parseByFactor(rawReadouts);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/mark-imports")
    public ResponseEntity<?> markImport(@RequestBody RawReadout rawReadout) {
        return ResponseEntity.ok().body(rawReadoutRepository.save(rawReadout));
    }
}
