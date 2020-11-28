/*
 * PermissionsEx
 * Copyright (C) zml and PermissionsEx contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ca.stellardrift.permissionsex.minecraft.command;

import ca.stellardrift.permissionsex.minecraft.MinecraftPermissionsEx;
import cloud.commandframework.execution.preprocessor.CommandPreprocessingContext;
import cloud.commandframework.execution.preprocessor.CommandPreprocessor;
import org.checkerframework.checker.nullness.qual.NonNull;

public class PEXCommandPreprocessor implements CommandPreprocessor<Commander> {

    public static final String PEX_MANAGER = "permissionsex:manager";

    private final MinecraftPermissionsEx<?> manager;

    public PEXCommandPreprocessor(MinecraftPermissionsEx<?> manager) {
        this.manager = manager;
    }

    @Override
    public void accept(@NonNull CommandPreprocessingContext<Commander> ctx) {
        ctx.getCommandContext().store(PEX_MANAGER, this.manager);
    }
}
