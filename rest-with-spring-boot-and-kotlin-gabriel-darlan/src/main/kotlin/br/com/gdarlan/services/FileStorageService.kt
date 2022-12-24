package br.com.gdarlan.services

import br.com.gdarlan.config.FileStorageConfig
import br.com.gdarlan.exceptions.FileStorageException
import br.com.gdarlan.exceptions.MyFileNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.springframework.util.StringUtils
import java.nio.file.StandardCopyOption

@Service
class FileStorageService @Autowired constructor(fileStorageConfig: FileStorageConfig) {

    private val fileStorageLocation: Path

    init {
        fileStorageLocation = Paths.get(fileStorageConfig.uploadDir).toAbsolutePath().normalize()
        try {
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileStorageException("Could not create the directory where the uploaded files will be storage", e)
        }
    }

    fun storeFile(file: MultipartFile): String {
        val fileName = StringUtils.cleanPath(file.originalFilename!!)
        return try {
            if (fileName.contains(".."))
                throw FileStorageException("Sorry! filename contains invalid path sequence $fileName. ")
            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (e: Exception) {
            throw FileStorageException("Could not file $fileName. Please try again!", e)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())
            if(resource.exists()) resource
            else throw MyFileNotFoundException("File not found $fileName.")
        } catch (e: Exception) {
            throw MyFileNotFoundException("File not found $fileName.", e)
        }
    }


}