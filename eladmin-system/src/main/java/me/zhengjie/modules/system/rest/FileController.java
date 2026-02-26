package me.zhengjie.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.config.FileProperties;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.utils.FileUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "工具：通用文件管理")
@RequestMapping("/api/file")
public class FileController {

    private final FileProperties properties;

    @Log("上传文件")
    @ApiOperation("通用文件上传")
    @PostMapping(value = "/upload")
    public ResponseEntity<Map<String, String>> upload(
            @RequestParam("file") MultipartFile file,
            @ApiParam("存储目录，如 article；不传则存到默认 file 目录")
            @RequestParam(value = "dir", required = false) String dir
    ) {
        FileUtil.checkSize(properties.getMaxSize(), file.getSize());
        String storePath = resolveStorePath(dir);
        File uploaded = FileUtil.upload(file, storePath);
        if (uploaded == null) {
            throw new BadRequestException("上传失败");
        }
        String dirParam = (dir != null && !dir.isEmpty()) ? dir : "";
        Map<String, String> result = new HashMap<>(3);
        result.put("fileName", file.getOriginalFilename());
        result.put("filePath", uploaded.getName());
        result.put("fileUrl", "/api/file/download" +
                (dirParam.isEmpty() ? "" : "?dir=" + dirParam + "&") +
                (dirParam.isEmpty() ? "?" : "") +
                "name=" + URLEncoder.encode(uploaded.getName(), StandardCharsets.UTF_8));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation("通用文件下载/预览")
    @AnonymousGetMapping(value = "/download")
    public void download(
            @ApiParam("存储目录") @RequestParam(value = "dir", required = false) String dir,
            @ApiParam("文件名（存储后的文件名）") @RequestParam("name") String name,
            HttpServletResponse response
    ) throws java.io.IOException {
        if (name.contains("..") || name.contains("/") || name.contains("\\")) {
            throw new BadRequestException("非法文件名");
        }
        String storePath = resolveStorePath(dir);
        File file = new File(storePath, name).getCanonicalFile();
        if (!file.exists()) {
            log.warn("文件不存在: {}", file.getAbsolutePath());
            throw new BadRequestException("文件不存在");
        }
        String contentType = guessContentType(name);
        String encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8).replace("+", "%20");
        response.setContentType(contentType);
        response.setContentLengthLong(file.length());
        response.setHeader("Content-Disposition", "inline; filename*=UTF-8''" + encodedName);
        Files.copy(file.toPath(), response.getOutputStream());
    }

    private String resolveStorePath(String dir) {
        String raw;
        if (dir == null || dir.isEmpty()) {
            raw = properties.getPath().getPath();
        } else if ("article".equals(dir)) {
            raw = properties.getPath().getArticle();
        } else {
            raw = properties.getPath().getPath() + dir + File.separator;
        }
        if (raw.startsWith("~")) {
            raw = System.getProperty("user.home") + raw.substring(1);
        }
        return raw;
    }

    private String guessContentType(String fileName) {
        String ext = FileUtil.getExtensionName(fileName);
        if (ext == null) return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        switch (ext.toLowerCase()) {
            case "pdf":  return "application/pdf";
            case "doc":  return "application/msword";
            case "docx": return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "xls":  return "application/vnd.ms-excel";
            case "xlsx": return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            case "png":  return "image/png";
            case "jpg": case "jpeg": return "image/jpeg";
            case "gif":  return "image/gif";
            case "mp4":  return "video/mp4";
            case "avi":  return "video/x-msvideo";
            case "mkv":  return "video/x-matroska";
            case "mov":  return "video/quicktime";
            case "wmv":  return "video/x-ms-wmv";
            case "txt":  return "text/plain";
            case "html": return "text/html";
            default:     return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
