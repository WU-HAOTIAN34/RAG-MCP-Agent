package com.wht.ai2025.tool;

import cn.hutool.core.io.FileUtil;
import com.wht.ai2025.constant.CommonConstant;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.io.File;

/**
 * 文件操作工具类（提供文件读写功能）
 */
public class FileOperationTool {

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of a file to read") String fileName) {
        String filePath = CommonConstant.FILE_FOLDER + File.separator + fileName;
        try {
            return FileUtil.readUtf8String(filePath);
        } catch (Exception e) {
            return "Error reading file: " + e.getMessage();
        }
    }

    @Tool(description = "Write content to a file")
    public String writeFile(@ToolParam(description = "Name of the file to write") String fileName,
                            @ToolParam(description = "Content to write to the file") String content
    ) {
        String filePath = CommonConstant.FILE_FOLDER + File.separator + fileName;

        try {
            // 创建目录
            FileUtil.mkdir(CommonConstant.FILE_FOLDER);
            FileUtil.writeUtf8String(content, filePath);
            return "File written successfully to: " + filePath;
        } catch (Exception e) {
            return "Error writing to file: " + e.getMessage();
        }
    }
}
