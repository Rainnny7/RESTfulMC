/*
 * MIT License
 *
 * Copyright (c) 2024 Braydon (Rainnny).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cc.restfulmc.sdk.serializer;

import cc.restfulmc.sdk.client.RESTfulMCClient;
import cc.restfulmc.sdk.response.server.dns.DNSRecord;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * A custom deserializer for {@link DNSRecord}'s.
 * <p>
 * This serializer will take the "type" in a DNS
 * record, and convert the record to the correct type.
 * E.g: A -> ARecord, SRV -> SRVRecord, etc
 * </p>
 *
 * @author Braydon
 */
public final class DNSRecordSerializer implements JsonDeserializer<DNSRecord> {
    @Override
    public DNSRecord deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        DNSRecord.Type recordType = DNSRecord.Type.valueOf(jsonElement.getAsJsonObject().get("type").getAsString()); // Get the record type
        return RESTfulMCClient.GSON.fromJson(jsonElement, recordType.getRecordClass()); // Convert the record to the correct type
    }
}