DESKTOP_JAR=$(CURDIR)/desktop/build/libs/desktop-1.0.jar
TOOLS_JAR=$(CURDIR)/tools/build/libs/tools-1.0.jar
GRADLEW=./gradlew
ifdef OFFLINE
	GRADLEW=./gradlew --offline
endif

GAME_CP=com.agateau.pixelwheels
EXECUTABLE=pixelwheels

include version.properties

PACKR=tools/packr.jar
PACKR_OUT_DIR=packr-out

DIST_OUT_BASE_DIR=dist-out
DIST_NAME=$(EXECUTABLE)-$(VERSION)
DIST_OUT_DIR=$(DIST_OUT_BASE_DIR)/$(DIST_NAME)

JDK_LINUX64_URL=https://bitbucket.org/alexkasko/openjdk-unofficial-builds/downloads/openjdk-1.7.0-u60-unofficial-linux-amd64-image.zip
JDK_LINUX64_ZIP=openjdk-linux64.zip

ARCHIVE_DIR=$(CURDIR)/archives

ifdef SNAPSHOT
	VERSION:=$(VERSION).$(shell date +%y%m%d-%H%M)
endif

all: build

clean: clean-packr clean-assets
	rm -f $(DESKTOP_JAR) $(TOOLS_JAR)

$(TOOLS_JAR):
	${GRADLEW} tools:dist

build:
	${GRADLEW} desktop:dist

tools: $(TOOLS_JAR)

run: build
	cd android/assets && java -jar $(DESKTOP_JAR)

packer: tools assets
	java -cp $(TOOLS_JAR) $(GAME_CP).tools.Packer

mapscreenshotgenerator: tools
	java -cp $(TOOLS_JAR) $(GAME_CP).tools.MapScreenshotGenerator

assets:
	$(MAKE) -C core/assets-src

clean-assets:
	$(MAKE) -C core/assets-src clean

# Packr
$(JDK_LINUX64_ZIP):
	wget $(JDK_LINUX64_URL) -O $(JDK_LINUX64_ZIP)

$(PACKR_OUT_DIR)/$(EXECUTABLE): $(JDK_LINUX64_ZIP) build
	java -jar $(PACKR) \
		-platform linux64 \
		-jdk $(JDK_LINUX64_ZIP) \
		-executable $(EXECUTABLE) \
		-appjar $(DESKTOP_JAR) \
		-mainclass com/agateau/pixelwheels/desktop/DesktopLauncher \
		-outdir $(PACKR_OUT_DIR) \
		-minimizejre soft
	cd android/assets && cp -r maps screens ui sprites.atlas sprites.png uiskin.atlas $(PACKR_OUT_DIR)

packr: $(PACKR_OUT_DIR)/$(EXECUTABLE)

clean-packr:
	rm -rf $(PACKR_OUT_DIR)

# Dist
desktop-dist: build
	@rm -rf $(DIST_OUT_DIR)
	@mkdir -p $(DIST_OUT_DIR)

	@echo Copying files
	@cp $(DESKTOP_JAR) $(DIST_OUT_DIR)/$(EXECUTABLE).jar
	chmod +x $(DIST_OUT_DIR)/$(EXECUTABLE).jar
	@cp -a install/* $(DIST_OUT_DIR)/

	@echo Creating zip
	@cd $(DIST_OUT_BASE_DIR) && zip -r $(DIST_NAME).zip $(DIST_NAME)
	@rm -rf $(DIST_OUT_DIR)

	@echo Moving zip
	@mkdir -p $(ARCHIVE_DIR)
	@mv $(DIST_OUT_DIR).zip $(ARCHIVE_DIR)

apk-dist:
	@echo Creating .apk
	@$(GRADLEW) android:assembleRelease
	@echo Moving .apk
	@mkdir -p $(ARCHIVE_DIR)
	@mv android/build/outputs/apk/release/android-release.apk $(ARCHIVE_DIR)/$(EXECUTABLE)-$(VERSION).apk

dist: assets packer check desktop-dist apk-dist

clean-dist: clean dist

# Tag
tag:
	git tag -f -m "Pixel Wheels $(VERSION)" $(VERSION)

tagpush: tag
	git push
	git push --tags

# Cleaning conf
clean-desktop-conf:
	rm -f ~/.config/agateau.com/pixelwheels.conf
	rm -rf ~/.local/share/pixelwheels

clean-android-conf:
	adb shell "pm clear com.agateau.tinywheels.android"

check:
	@$(GRADLEW) check
	@$(GRADLEW) test

.PHONY: desktop-dist apk-dist dist clean-dist tag tagpush check tools build release-archives
