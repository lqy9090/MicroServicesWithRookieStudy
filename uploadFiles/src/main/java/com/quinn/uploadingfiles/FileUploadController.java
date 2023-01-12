package com.quinn.uploadingfiles;

import cn.hutool.core.io.FileTypeUtil;
import com.quinn.uploadingfiles.exception.StorageFileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: qiuyi
 * @Description:
 * @DateTime: 2023/1/6 10:22
 **/
@Controller
@RequestMapping("/api/v1/storage")
public class FileUploadController {
    private final StorageService storageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) throws IOException {

        model.addAttribute("files", storageService.loadAll().map(
                        path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "serveFile", path.getFileName().toString()).build().toUri().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException {

        System.out.println("file contentType: " + file.getContentType());
        System.out.println("inputStream: " + bytesToHexString(file.getInputStream().readAllBytes()));
        //限制文件类型的集合
        Set<String> fileType = Set.of("jpg");

        //检查文件类型
        String type = FileTypeUtil.getType(file.getInputStream());
        System.out.println("type: " + type);

        Boolean canStoreFile = Optional.ofNullable(type).map(fileType::contains).orElse(false)
                || "text/plain".equals(file.getContentType());

        System.out.println("canStoreFile: " + canStoreFile);

        // 判断是否在限制的文件类型集合中，如果为null，直接返回false
        if (!canStoreFile) {
            redirectAttributes.addFlashAttribute("message",
                    "You failed to uploaded " + file.getOriginalFilename() + "," +
                            "with type " + type + ".");

            System.out.println("faild uploaded" + file.getOriginalFilename());
        } else {
            storageService.store(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");

            System.out.println("successfully uploaded" + file.getOriginalFilename());
        }

        return "redirect:/api/v1/storage/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}