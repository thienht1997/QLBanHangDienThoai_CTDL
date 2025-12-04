package com.ctdl.btl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FileService {

    public List<Phone> readPhones(Path path) throws IOException {
        if (Files.notExists(path)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(path).stream()
                .filter(line -> !line.isBlank())
                .map(Phone::fromCsv)
                .collect(Collectors.toList());
    }

    public void writePhones(Path path, List<Phone> phones) throws IOException {
        ensureParent(path);
        Files.write(path, phones.stream()
                .map(Phone::toCsv)
                .collect(Collectors.toList()));
    }

    public List<Invoice> readInvoices(Path path) throws IOException {
        if (Files.notExists(path)) {
            return Collections.emptyList();
        }
        return Files.readAllLines(path).stream()
                .filter(line -> !line.isBlank())
                .map(Invoice::fromCsv)
                .collect(Collectors.toList());
    }

    public void writeInvoices(Path path, List<Invoice> invoices) throws IOException {
        ensureParent(path);
        Files.write(path, invoices.stream()
                .map(Invoice::toCsv)
                .collect(Collectors.toList()));
    }

    private void ensureParent(Path path) throws IOException {
        Path parent = path.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }
}
