APP=$(shell basename -s .git $(shell git remote get-url origin))
REGISTRY=ghcr.io/inezhinskiy
VERSION=$(shell git describe --tags --abbrev=0)-$(shell git rev-parse --short HEAD)
#linux darwin windows
TARGETOS=linux
#arm64 amd64
TARGETARCH=amd64

format:
	gofmt -s -w ./ 

get:
	go get

lint:
	golint

test:
	go test -v

build: format
	CGO_ENABLED=0 GOOS=$(TARGETOS) GOARCH=$(TARGETARCH) go build -v -o kbot -ldflags "-X="github.com/inezhinskiy/kbot/cmd.appVersion=$(VERSION)

image:
	docker build . -t ${REGISTRY}/${APP}:${VERSION}-${TARGETOS}-${TARGETARCH}

push: 
	docker push ${REGISTRY}/${APP}:${VERSION}-${TARGETOS}-${TARGETARCH}

clean:
	rm -rf kbot