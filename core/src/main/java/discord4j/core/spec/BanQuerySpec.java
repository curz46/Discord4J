/*
 * This file is part of Discord4J.
 *
 * Discord4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Discord4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Discord4J.  If not, see <http://www.gnu.org/licenses/>.
 */
package discord4j.core.spec;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class BanQuerySpec implements AuditSpec<Map<String, Object>> {

    private final Map<String, Object> request = new HashMap<>(2);

    public BanQuerySpec setDeleteMessageDays(final int days) {
        request.put("delete-message-days", days);
        return this;
    }

    @Override
    public BanQuerySpec setReason(@Nullable final String reason) {
        request.put("reason", reason);
        return this;
    }

    @Override
    @Nullable
    public String getReason() {
        return (String) request.get("reason");
    }

    @Override
    public Map<String, Object> asRequest() {
        return request;
    }
}
