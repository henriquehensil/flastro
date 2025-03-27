package dev.hensil.flastro.api.file;

import dev.hensil.flastro.api.user.User;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import java.net.URI;
import java.nio.file.attribute.FileTime;

import dev.hensil.flastro.api.user.Root;

public interface MetaFile {

    @NotNull Dataset getDataset();

    @NotNull Times getTime();

    // Classes

    interface Dataset {

        @NotNull String getName();

        @NotNull Type getType();

        @NotNull String getParent();

        @NotNull URI getURI();

        // Classes

        interface Data {

            /**
             * Represent the start index of the data array.
             * */
            int startIndex();

            /**
             * Returns the length of data in the array.
             * <p>
             * The end index can be calculated as: {@code startIndex() + length() - 1}.
             * */
            int length();

            /**
             * Returns the original (uncompressed) size of the data.
             * <p>
             * This value always represent the uncompressed size, unlike {@link Data#length()}
             * which may refer to a compressed size in certain context.
             *
             * @return the original size of the data.
             * */
            @Range(from = 0, to = Long.MAX_VALUE) long size();
        }

        /**
         * The type of file
         * */
        enum Type {
            FOLDER,

            /**
             * Regular file
             * */
            FILE,
        }
    }

    interface Times {

        @NotNull Time getLastModified();

        @NotNull Time getLastAccess();

        @NotNull Time getCreation();

        // Classes

        interface Time {

            @NotNull FileTime getFileTime();

            @NotNull User getUser();
        }
    }

    interface Controls {

        boolean contains(@NotNull Access access);

        @NotNull Access @NotNull [] getAccessArray();

        // Classes

        enum Access {
            /**
             * Represents a hidden file that can only seen by the {@link Root}
             * */
            HIDDEN,

            /**
             * Represents a locked file that cannot be edited, moved, written to, or modified in any way.
             * <p>
             * A locked file is static, meaning its contents and properties are immutable during the lock state.
             * This ensures that no changes can be made to the file, protecting it from accidental or unauthorized modifications.
             * */
            LOCK,
            READ_ONLY,
            WRITE_ONLY,
        }
    }
}