package com.tgz;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.module.annotations.ReactModule;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

@ReactModule(name = TgzModule.NAME)
public class TgzModule extends ReactContextBaseJavaModule {
  public static final String NAME = "Tgz";

  public TgzModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  /**
   * "file:///data/user"  X
   * "/data/user"         √
   */
  final String formatPath(String path) {
    return path.replace("file://", "");
  }

  /**
   * @param archivePath .tgz file path
   * @param targetDir target dir path
   * @example decompress("test.tgz", "temp") will create a directory "temp/test"
   * @attention auto cover existing files!!!
   */
  @ReactMethod
  public void decompress(String archivePath, String targetDir, Promise promise) {

    archivePath = formatPath(archivePath);
    targetDir = formatPath(targetDir);
    try {
      File archiveFile = new File(archivePath);
      if (!archiveFile.exists()) {
        promise.reject(null, "archive not exists: " + archivePath);
        return;
      }

      GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(archiveFile));
      TarArchiveInputStream tarArchiveInputStream = new TarArchiveInputStream(gzipInputStream);
      TarArchiveEntry entry = null;

      while ((entry = (TarArchiveEntry) tarArchiveInputStream.getNextEntry()) != null) {
        File file = new File(targetDir, entry.getName());
        // since file will call getParentFile().mkdirs()
        // we don't need to handle directory entry
        if (!entry.isDirectory()) {
          // if parent dir not exists, create it and its ancestors
          if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
          }

          int byteCount;
          byte buffer[] = new byte[2048];
          FileOutputStream fileOutputStream = new FileOutputStream(file);
          while ((byteCount = tarArchiveInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, byteCount);
          }
          fileOutputStream.close();
        }
      }
      promise.resolve(targetDir);
    } catch (IOException e) {
      e.printStackTrace();
      promise.reject(null, e.toString());
    }
  }
}
