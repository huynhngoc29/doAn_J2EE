package nhom02.doanmon.controller;

import nhom02.doanmon.dto.CommitResult;
import nhom02.doanmon.dto.ImportResult;
import nhom02.doanmon.dto.ProductImportDto;
import nhom02.doanmon.service.ImportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/import/products")
public class ImportController {

    @Autowired
    private ImportService importService;

    @PostMapping("/preview")
    public ResponseEntity<ImportResult> preview(@RequestParam("file") MultipartFile file) {
        try {
            ImportResult result = importService.preview(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/commit")
    public ResponseEntity<CommitResult> commit(@RequestBody List<ProductImportDto> validRows) {
        try {
            CommitResult result = importService.commit(validRows);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
