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
package ca.stellardrift.permissionsex.sponge

import ca.stellardrift.permissionsex.minecraft.command.Commander
import ca.stellardrift.permissionsex.minecraft.command.MessageFormatter
import ca.stellardrift.permissionsex.subject.SubjectRef
import ca.stellardrift.permissionsex.util.styled
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.NamedTextColor
import org.spongepowered.api.command.CommandSource
import org.spongepowered.api.service.pagination.PaginationService

/**
 * An abstraction over the Sponge CommandSource that handles PEX-specific message formatting and localization
 */
internal class SpongeCommander(
    val pex: PermissionsExPlugin,
    internal val commandSource: CommandSource
) : Commander {
    private val formatter = SpongeMessageFormatter(pex)
    private val audience = pex.adventure.receiver(commandSource)

    override fun hasPermission(permission: String): Boolean {
        return commandSource.hasPermission(permission)
    }

    override fun sendPaginated(
        title: Component,
        header: Component?,
        text: Iterable<ComponentLike>
    ) {
        val build =
            pex.game.serviceManager.provide(
                PaginationService::class.java
            ).get().builder()
        formatter.apply {
            build.title(title.styled { header(hl(this)) }.toSponge())
            if (header != null) {
                build.header(header.color(NamedTextColor.GRAY).toSponge())
            }
            build.contents(text.map { it.asComponent().color(this.responseColor()).toSponge() })
                .sendTo(commandSource)
        }
    }

    override fun audience(): Audience {
        return this.audience
    }

    override fun name(): Component {
        return text(commandSource.name)
    }

    override fun subjectIdentifier(): SubjectRef<*>? {
        return PEXSubjectReference.of(
            commandSource.asSubjectReference(),
            pex
        )
    }

    override fun formatter(): MessageFormatter {
        return this.formatter
    }
}

internal class SpongeMessageFormatter(plugin: PermissionsExPlugin) : MessageFormatter(plugin.mcManager) {

    override fun <I : Any?> friendlyName(reference: SubjectRef<I>): String? {
        return (reference.type().getAssociatedObject(reference.identifier()) as? CommandSource)?.name
    }
}
