package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import nhom02.doanmon.dto.CakeImportDto;
import nhom02.doanmon.dto.CakeImportResult;
import nhom02.doanmon.dto.CommitResult;
import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.service.CakeImportExportService;
import nhom02.doanmon.service.CakeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/cakes")
public class AdminController {

    @Autowired
    private CakeService cakeService;

    @Autowired
    private nhom02.doanmon.service.CategoryService categoryService;

    @Autowired
    private CakeImportExportService importExportService;

    @GetMapping
    public String listCakes(Model model) {
        model.addAttribute("cakes", cakeService.findAll());
        return "admin/cakes/list";
    }

    @GetMapping("/add")
    public String addCakeForm(Model model) {
        model.addAttribute("cake", new Cake());
        model.addAttribute("categories", categoryService.findAll());
        return "admin/cakes/form";
    }

    @GetMapping("/save")
    public String redirectSaveToCakes() {
        return "redirect:/admin/cakes";
    }

    @PostMapping("/save")
    public String saveCake(@ModelAttribute("cake") Cake cake,
            @RequestParam(value = "imageFile", required = false) org.springframework.web.multipart.MultipartFile imageFile,
            @RequestParam(value = "model3File", required = false) org.springframework.web.multipart.MultipartFile model3File) {

        // Handle retaining existing files when editing
        if (cake.getId() != null) {
            Cake existingCake = cakeService.findById(cake.getId()).orElse(null);
            if (existingCake != null) {
                if (imageFile == null || imageFile.isEmpty()) {
                    cake.setImage(existingCake.getImage());
                }
                if (model3File == null || model3File.isEmpty()) {
                    cake.setModel3D(existingCake.getModel3D());
                }
            }
        }

        // Helper logic to find correct project directory
        String projectDir = System.getProperty("user.dir");
        if (!projectDir.endsWith("doanmon") && !projectDir.endsWith("doanmon\\") && !projectDir.endsWith("doanmon/")) {
            // CWD might be the parent directory (e.g. e:\java_SpringBoot\doAn_J2EE)
            projectDir = java.nio.file.Paths.get(projectDir, "doanmon").toString();
        }

        // Image upload
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imgName = org.springframework.util.StringUtils.cleanPath(imageFile.getOriginalFilename());
                java.nio.file.Path imgUploadPath = java.nio.file.Paths.get(projectDir,
                        "src/main/resources/static/img/");
                if (!java.nio.file.Files.exists(imgUploadPath)) {
                    java.nio.file.Files.createDirectories(imgUploadPath);
                }
                java.nio.file.Path imgPath = imgUploadPath.resolve(imgName);
                java.nio.file.Files.copy(imageFile.getInputStream(), imgPath,
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                cake.setImage("/img/" + imgName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 3D Model upload
        if (model3File != null && !model3File.isEmpty()) {
            try {
                String fileName = org.springframework.util.StringUtils.cleanPath(model3File.getOriginalFilename());
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(projectDir, "src/main/resources/static/model/");
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                }
                java.io.InputStream inputStream = model3File.getInputStream();
                java.nio.file.Path filePath = uploadPath.resolve(fileName);
                java.nio.file.Files.copy(inputStream, filePath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);

                cake.setModel3D(fileName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cakeService.save(cake);
        return "redirect:/admin/cakes";
    }

    @GetMapping("/edit/{id}")
    public String editCakeForm(@PathVariable Long id, Model model) {
        model.addAttribute("cake", cakeService.findById(id).orElse(null));
        model.addAttribute("categories", categoryService.findAll());
        return "admin/cakes/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteCake(@PathVariable Long id) {
        cakeService.deleteById(id);
        return "redirect:/admin/cakes";
    }

    /**
     * XUẤT danh sách bánh ra Excel
     */
    @GetMapping("/export")
    @ResponseBody
    public ResponseEntity<byte[]> exportCakes() {
        try {
            byte[] excelData = importExportService.exportCakesToExcel();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", "cakes_export.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(excelData);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * PREVIEW file Excel trước khi import
     */
    @PostMapping("/import/preview")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> previewImport(@RequestParam("file") MultipartFile file) {
        try {
            CakeImportResult result = importExportService.previewImport(file);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi khi đọc file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    /**
     * COMMIT - Lưu dữ liệu vào database
     */
    @PostMapping("/import/commit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> commitImport(@RequestBody List<CakeImportDto> validRows) {
        try {
            CommitResult result = importExportService.commitImport(validRows);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("result", result);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lỗi khi lưu dữ liệu: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
