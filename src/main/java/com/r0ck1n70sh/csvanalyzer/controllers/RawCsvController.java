package com.r0ck1n70sh.csvanalyzer.controllers;

import com.r0ck1n70sh.csvanalyzer.crud.RawCsvMetaCrud;
import com.r0ck1n70sh.csvanalyzer.csv.CsvParser;
import com.r0ck1n70sh.csvanalyzer.entities.RawCsvMeta;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvColumnRepository;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvDataPointRepository;
import com.r0ck1n70sh.csvanalyzer.repositories.RawCsvMetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/api/raw_csv/")
public class RawCsvController {
    @Autowired
    private RawCsvMetaRepository metaRepository;

    @Autowired
    private RawCsvColumnRepository columnRepository;

    @Autowired
    private RawCsvDataPointRepository dataPointRepository;

    @GetMapping(path = {"/"})
    @ResponseBody
    public List<RawCsvMeta> get() {
        Iterable<RawCsvMeta> iterable = metaRepository.findAll();
        return StreamSupport.stream(iterable.spliterator(), false).toList();
    }

    @GetMapping("/{name}")
    @ResponseBody
    public RawCsvMeta getByName(@PathVariable String name) {
        Iterable<RawCsvMeta> iterable = metaRepository.findAll();

        return StreamSupport.stream(iterable.spliterator(), false)
                .filter(rawCsvMeta -> rawCsvMeta.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @PostMapping("/")
    @ResponseBody
    public RawCsvMeta create(@RequestBody Map<String, String> body) {
        String name = body.get("name");
        String base64 = body.get("base64");

        byte[] bytes = Base64.getDecoder().decode(base64);
        String data = new String(bytes);

        CsvParser csvParser = new CsvParser(data, name);
        RawCsvMeta rawCsvMeta = csvParser.parse();

        RawCsvMetaCrud.save(rawCsvMeta);

        return rawCsvMeta;
    }

}
