//package ghostface.dev.impl;
//
//import ghostface.dev.exception.NameAlreadyExistsException;
//import ghostface.dev.file.FileStorage;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//import org.jetbrains.annotations.Unmodifiable;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//
//public final class FileStorageImpl implements FileStorage {
//
//    // Logger
//    private static final Logger log = LogManager.getLogger(FileStorageImpl.class);
//
//    private final @NotNull FilesImpl files = new FilesImpl();
//    private final @NotNull DatabaseImpl database;
//    private final @NotNull Path path;
//
//    FileStorageImpl(@NotNull DatabaseImpl database, @NotNull Path path) {
//        this.database = database;
//        this.path = path;
//    }
//
//    @Override
//    public @NotNull DatabaseImpl database() {
//        return database;
//    }
//
//    @Override
//    public @NotNull Path getDefault() {
//        return path;
//    }
//
//    @Override
//    public @NotNull FilesImpl getFiles() {
//        return files;
//    }
//
//    // Classes
//
//    public final class FilesImpl implements Files {
//
//        private final @NotNull Map<@NotNull String, @NotNull File> fileMap = new ConcurrentHashMap<>();
//
//        private FilesImpl() {
//        }
//
//        @Override
//        public @NotNull FileStorage getFileStorage() {
//            return FileStorageImpl.this;
//        }
//
//        @Override
//        public @NotNull Optional<@NotNull File> get(@NotNull Path path) {
//            if (path.isAbsolute() && path.startsWith(getDefault())) {
//                return Optional.ofNullable(fileMap.get(getDefault().relativize(path).toString().toLowerCase()));
//            } else {
//                return Optional.ofNullable(fileMap.get(path.toString().toLowerCase()));
//            }
//        }
//
//        @Override
//        public @NotNull File create(@NotNull String name) throws NameAlreadyExistsException, IOException {
//            @NotNull String pathStr = name.replace(" ", "_").replace("/", "_").replace("\\", "_").toLowerCase();
//
//            if (fileMap.containsKey(pathStr.toLowerCase())) {
//                log.warn("Name already exist. Formated: {} Brute {}", pathStr, name);
//                throw new NameAlreadyExistsException("This name is already in use: " + name);
//            }
//
//            @NotNull File file = getDefault().resolve(pathStr).toFile();
//
//            if (!file.isFile() && !file.createNewFile()) {
//                log.warn("File cannot be created in Path: {}", file.toPath());
//                throw new FileNotFoundException("Cannot create this file: " + file.getAbsolutePath());
//            }
//
//            fileMap.put(pathStr, file);
//            log.info("Success! File created and added in Storage: {}", pathStr);
//            return file;
//        }
//
//        @Override
//        public @NotNull File create(@NotNull String folder, @NotNull String name) throws NameAlreadyExistsException, IOException {
//            @NotNull String nameStr = name.replace(" ", "_").replace("/", "_").replace("\\", "_").toLowerCase();
//            @NotNull String folderStr = folder.replace(" ", "_").toLowerCase();
//
//            if (nameStr.startsWith("_")) {
//                nameStr = nameStr.replaceFirst("_", "");
//            }
//
//            @NotNull Path path = Paths.get(folderStr, nameStr);
//
//            if (fileMap.containsKey(path.toString())) {
//                log.warn("Name already exist. Formated: {} Brute {}", path, (folder + name));
//                throw new NameAlreadyExistsException("Name is already in use");
//            }
//
//            // creating folders
//            @NotNull File file = getDefault().resolve(path).getParent().toFile();
//
//            if (!file.isDirectory() && !file.mkdirs()) {
//                log.warn("Cannot created new folders {}", file.toPath());
//                throw new FileNotFoundException("Cannot create folders: " + file.getAbsolutePath());
//            }
//
//            // creating fale
//            file = getDefault().resolve(path).toFile();
//            log.warn("Creating new file: {}", file.toPath());
//
//            if (!file.isFile() && !file.createNewFile()) {
//                log.warn("cannot created new file {}", file.toPath());
//                throw new FileNotFoundException("Cannot create file: " + file.getAbsolutePath());
//            }
//
//            @NotNull String key = getDefault().relativize(file.toPath()).toString();
//            fileMap.put(key.toLowerCase(), file);
//            log.info("Success! file created and added in Storage: {}", key.toLowerCase());
//
//            return file;
//        }
//
//        @Override
//        public @NotNull Optional<@NotNull File> get(@NotNull String name) {
//            return Optional.ofNullable(fileMap.get(name.startsWith("\\") ? name.replaceFirst("^\\\\", "").toLowerCase() : name.toLowerCase()));
//        }
//
//        @Override
//        public boolean delete(@NotNull String name) {
//            @Nullable File file = get(name.toLowerCase()).orElse(null);
//
//            if (file == null) return false;
//
//            return file.delete() && fileMap.remove(name) != null;
//        }
//
//        @Override
//        public boolean delete(@NotNull Path path) {
//            if (path.isAbsolute() && path.startsWith(getDefault())) {
//                return fileMap.remove(getDefault().relativize(path).toString().toLowerCase()) != null;
//            } else {
//                return fileMap.remove(path.toString().toLowerCase()) != null;
//            }
//        }
//
//        @Override
//        public @Unmodifiable @NotNull Collection<@NotNull File> toCollection() {
//            return Collections.unmodifiableCollection(fileMap.values());
//        }
//    }
//}