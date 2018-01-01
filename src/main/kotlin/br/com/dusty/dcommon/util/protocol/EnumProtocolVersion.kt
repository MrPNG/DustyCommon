package br.com.dusty.dcommon.util.protocol

import br.com.dusty.dcommon.util.protocol.EnumProtocolSafety.*
import br.com.dusty.dcommon.util.text.color

enum class EnumProtocolVersion(val min: Int, val max: Int, string: String, safety: EnumProtocolSafety) {

	UNKNOWN(-1, -1, "unknown", TERRIBLE),
	PRE_1_7(0, 3, "pre-1.7", TERRIBLE),
	RELEASE_1_7_2(4, 4, "1.7.2-1.7.5", BAD),
	RELEASE_1_7_10(5, 5, "1.7.6-1.7.10", REGULAR),
	SNAPSHOT_1_8(6, 46, "snapshot_1.8", TERRIBLE),
	RELEASE_1_8(47, 47, "1.8.0-1.8.9", EXCELLENT),
	SNAPSHOT_1_9(48, 106, "snapshot_1.9", TERRIBLE),
	RELEASE_1_9(107, 107, "1.9.0", TERRIBLE),
	RELEASE_1_9_1(108, 108, "1.9.1", TERRIBLE),
	RELEASE_1_9_2(109, 109, "1.9.2", REGULAR),
	RELEASE_1_9_4(110, 110, "1.9.3-1.9.4", GOOD),
	SNAPSHOT_1_10(201, 205, "snapshot_1.10", TERRIBLE),
	RELEASE_1_10(210, 210, "1.10.0-1.10.2", GOOD),
	SNAPSHOT_1_11(301, 314, "snapshot_1.11", TERRIBLE),
	RELEASE_1_11(315, 315, "1.11.0", REGULAR),
	RELEASE_1_11_2(316, 316, "1.11.1-1.11.2", GOOD),
	SNAPSHOT_1_12(317, 334, "snapshot_1.12", TERRIBLE),
	RELEASE_1_12(335, 335, "1.12", BAD),
	SNAPSHOT_1_12_1(336, 337, "snapshot_1.12.1", TERRIBLE),
	RELEASE_1_12_1(338, 338, "1.12.1", BAD),
	SNAPSHOT_1_12_2(339, 339, "snapshot_1.12.2", TERRIBLE),
	RELEASE_1_12_2(340, 340, "1.12.2", GOOD);

	private val STRING = string.color(safety.color)

	fun isGreaterThan(protocolVersion: EnumProtocolVersion) = protocolVersion.max < min

	fun isGreaterThanOrEquals(protocolVersion: EnumProtocolVersion) = this == protocolVersion || protocolVersion.max < min

	fun isLowerThan(protocolVersion: EnumProtocolVersion) = protocolVersion.min > max

	fun isLowerThanOrEquals(protocolVersion: EnumProtocolVersion) = this == protocolVersion || protocolVersion.min > max

	override fun toString() = STRING

	companion object {

		operator fun get(version: Int) = values().firstOrNull { it.min <= version && it.max >= version } ?: UNKNOWN
	}
}
