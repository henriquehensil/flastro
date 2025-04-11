package dev.hensil.flastro.core.file;

import dev.hensil.flastro.core.storage.Storage;
import org.jetbrains.annotations.NotNull;

import java.io.FileDescriptor;
import java.util.Collection;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.NoSuchFileException;
import java.nio.file.FileAlreadyExistsException;

import java.util.Optional;

public interface FileStorage extends Storage {

    @NotNull Manager getManager();

    @Override
    default byte getCode() {
        return 0;
    }

    // Classes

    // todo FilePermissionException and another settings exception
    interface Manager {

        /**
         *
         * @param folder the folder id present in {@link FileRecord#getId()}.
         *
         * @return A recent file record
         *
         * @throws IllegalArgumentException if the {@code name} is an invalid file name (optional specific operation)
         * @throws NoSuchFileException if the {@code folder} id doest not exists
         * @throws NotDirectoryException if the {@code folder} is present, but is not a folder
         * @throws FileAlreadyExistsException when the {@code name} already is present in this folder
         * @throws IOException if another specific I/O error occurs
         * */
        @NotNull FileRecord createFile(@NotNull String folder, @NotNull String name, boolean mkdirs) throws IOException;

        /**
         *
         * @param folder the folder id present in {@link FileRecord#getId()}.
         *
         * @return A recent folder record
         *
         * @throws IllegalArgumentException if the {@code name} is an invalid folder name (optional specific operation)
         * @throws NoSuchFileException if the {@code folder} id doest not exists.
         * @throws NotDirectoryException if the {@code folder} is present, but is not a folder.
         * @throws FileAlreadyExistsException when the {@code name} already is present in this folder
         * @throws IOException if another specific I/O error occurs.
         * */
        @NotNull FolderRecord createFolder(@NotNull String folder, @NotNull String name, boolean mkdirs) throws IOException;

        @NotNull Optional<@NotNull FileRecord> get(@NotNull String fileId);

        /**
         * @throws NoSuchFileException if the file doest not exists.
         * @throws IOException if another error occurs.
         * */
        void delete(@NotNull String file) throws IOException;

        /**
         * @throws NoSuchFileException if one of the files does not exist.
         * @throws NotDirectoryException if the {@code target} file exist but is not a folder.
         * @throws IOException if another I/O error occurs.
         * */
        void move(@NotNull String file, @NotNull String fileTarget) throws IOException;

        /**
         * @throws NoSuchFileException if one of the files does not exist.
         * @throws NotDirectoryException if the {@code fileTarget} file exist but is not a folder.
         * @throws IOException if another I/O error occurs.
         * */
        void copy(@NotNull String file, @NotNull String fileTarget) throws IOException;

        /**
         * @throws IllegalArgumentException if the {@code newName} is illegal (optional specific operation).
         * @throws NoSuchFileException if the file doest not exists.
         * @throws IOException if a I/O e error occurs
         * */
        void rename(@NotNull String file, @NotNull String newName) throws IOException;

        @NotNull Collection<? extends @NotNull FileDescriptor> getFiles(@NotNull String folder) throws IOException;

    }

    /*
    *
    * Options
    *
    * */

//    interface MoveOption {
//
//        @NotNull MoveOption CREATE_IF_NOT_EXIST = Standard.CREATE;
//
//        @NotNull MoveOption ONLY_TARGET_IS_EMPTY = Standard.ONLY_TARGET_EMPTY;
//
//        @NotNull MoveOption REPLACE_EXISTING = Standard.REPLACE;
//
//        // Classes
//
//        enum Standard implements MoveOption {
//            CREATE,
//            ONLY_TARGET_EMPTY,
//            REPLACE
//            ;
//        }
//    }
//
//    interface CopyOption {
//
//        @NotNull CopyOption CREATE_IF_NOT_EXIST = Standard.CREATE;
//
//        @NotNull CopyOption COPY_ATTRIBUTES = Standard.ATTRIBUTES;
//
//        @NotNull CopyOption ONLY_TARGET_IS_EMPTY = Standard.ONLY_TARGET_EMPTY;
//
//        @NotNull CopyOption REPLACE_EXISTING = Standard.REPLACE;
//
//        // Classes
//
//        enum Standard implements CopyOption {
//            CREATE,
//            ATTRIBUTES,
//            ONLY_TARGET_EMPTY,
//            REPLACE,
//            ;
//        }
//    }
}