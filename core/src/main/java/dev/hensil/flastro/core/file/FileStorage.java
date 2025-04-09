package dev.hensil.flastro.core.file;

import dev.hensil.flastro.core.storage.Storage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.NoSuchFileException;

import dev.hensil.flastro.core.exception.IllegalOptionsException;

import java.util.Optional;
import java.util.Set;

public interface FileStorage extends Storage {

    @NotNull Manager getManager();

    // Classes

    interface Manager {

        // Objects

        default @NotNull Path getRootPath() {
            return Paths.get("/");
        }

        @NotNull MetaFile createFile(@NotNull Path parents, @NotNull String name, boolean mkdirs) throws IOException;

        @NotNull MetaFile createFolder(@NotNull Path parents, @NotNull String name, boolean mkdirs) throws IOException;

        @NotNull Optional<@NotNull MetaFile> get(@NotNull URI uri);

        /**
         * @throws NoSuchFileException if the file doest not exists.
         * @throws IllegalArgumentException if the file exists, but is a folder.
         * @throws IOException if another error occurs.
         * */
        void deleteFile(@NotNull URI uri) throws IOException;

        /**
         * @throws NoSuchFileException if the folder doest not exists.
         * @throws NotDirectoryException if the file is not a folder.
         * @throws DirectoryNotEmptyException if the folder is not empty and the {@code onlyIfIsEmpty} parameter is true.
         * @throws IOException if another error occurs.
         * */
        void deleteFolder(@NotNull URI uri, boolean onlyIfIsEmpty) throws IOException;

        /**
         * @throws IllegalOptionsException if the set contains an illegal option combination (optional specific operation).
         * @throws NoSuchFileException if the file or folder given by the both URI parameter doest not exists.
         * @throws IOException if another I/O error occurs.
         * */
        void move(@NotNull URI from, @NotNull URI target, @Nullable Set<@NotNull MoveOption> moveOption) throws IOException;

        /**
         * @throws IllegalOptionsException if the set contains an illegal option combination (optional specific operation).
         * @throws NoSuchFileException if the file or folder given by the both URI parameter doest not exists.
         * @throws IOException if another I/O error occurs.
         * */
        void copy(@NotNull URI from, @NotNull URI target, @Nullable Set<@NotNull CopyOption> copyOption) throws IOException;

        /**
         * @throws IllegalArgumentException if the {@code newName} is illegal (optional specific operation).
         * @throws NoSuchFileException if the file doest not exists.
         * @throws IOException if a I/O e error occurs
         * */
        void rename(@NotNull URI uri, @NotNull String newName) throws IOException;

        @NotNull Collection<? extends @NotNull MetaFile> getFiles(@NotNull Path location) throws IOException;

    }

    /*
    *
    * Options
    *
    * */

    interface MoveOption {

        @NotNull MoveOption CREATE_IF_NOT_EXIST = Standard.CREATE;

        @NotNull MoveOption ONLY_TARGET_IS_EMPTY = Standard.ONLY_TARGET_EMPTY;

        @NotNull MoveOption REPLACE_EXISTING = Standard.REPLACE;

        // Classes

        enum Standard implements MoveOption {
            CREATE,
            ONLY_TARGET_EMPTY,
            REPLACE
            ;
        }
    }

    interface CopyOption {

        @NotNull CopyOption CREATE_IF_NOT_EXIST = Standard.CREATE;

        @NotNull CopyOption COPY_ATTRIBUTES = Standard.ATTRIBUTES;

        @NotNull CopyOption ONLY_TARGET_IS_EMPTY = Standard.ONLY_TARGET_EMPTY;

        @NotNull CopyOption REPLACE_EXISTING = Standard.REPLACE;

        // Classes

        enum Standard implements CopyOption {
            CREATE,
            ATTRIBUTES,
            ONLY_TARGET_EMPTY,
            REPLACE,
            ;
        }
    }
}