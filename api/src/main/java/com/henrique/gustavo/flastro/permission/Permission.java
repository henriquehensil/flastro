package com.henrique.gustavo.flastro.permission;

import org.jetbrains.annotations.NotNull;

public interface Permission {

    @NotNull Permission CREATE_FILE = new Permission() {
        @Override
        public @NotNull String getName() {
            return "Create File";
        }

        @Override
        public @NotNull String getDescription() {
            return "Permission to create files";
        }

        @Override
        public byte getCode() {
            return 0x21;
        }
    };

    @NotNull Permission DELETE_FILE = new Permission() {
        @Override
        public @NotNull String getName() {
            return "Delete File";
        }

        @Override
        public @NotNull String getDescription() {
            return "Permission to delete files";
        }

        @Override
        public byte getCode() {
            return 0x22;
        }
    };

    @NotNull Permission READ_FILE = new Permission() {
        @Override
        public @NotNull String getName() {
            return "Read File";
        }

        @Override
        public @NotNull String getDescription() {
            return "Permission to read and modify files";
        }

        @Override
        public byte getCode() {
            return 0x41;
        }
    };

    // Objects

    @NotNull String getName();

    @NotNull String getDescription();

    byte getCode();

}