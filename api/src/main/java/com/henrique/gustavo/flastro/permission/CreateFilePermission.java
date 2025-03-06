package com.henrique.gustavo.flastro.permission;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.nio.file.Path;
import java.util.Set;
import java.util.regex.Pattern;

public interface CreateFilePermission extends Permission {

    @Unmodifiable @NotNull Set<@NotNull Path> getPermittedPaths();

    @Unmodifiable @NotNull Set<@NotNull String> getPermittedExtensions();

    @NotNull Pattern getFileNamePattern();

}