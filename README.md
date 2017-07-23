scalajs-minesweeper is an implementation of Minesweeper in Scala JS.

## How to run locally

    bin/sbt
    > ~fastOptJS`

Navigate to: http://localhost:12345/js/target/scala-2.12/classes/index-dev.html

The page will refresh on every change to the code.

Use `sbt fullOptJS` and open `index-opt.html` for the optimized version.

## Live version

https://s3.eu-west-2.amazonaws.com/scalajs-minesweeper/resources/index-opt.html

To upload a new version:

    bin/sbt 
    > fullOptJs
    > s3-upload

(this depends on having appropriate S3 credentials)

The S3 bucket is set up with a public read policy:

    {
      "Version":"2012-10-17",
      "Statement":[{
        "Sid":"PublicReadGetObject",
            "Effect":"Allow",
          "Principal": "*",
          "Action":["s3:GetObject"],
          "Resource":["arn:aws:s3:::scalajs-minesweeper/*"
          ]
        }
      ]
    }

## Frontend tests

  > brew install nodejs
  
  > brew install cairo
  
  > npm install jsdom
  
  > npm install cairo
  
  > npm install canvas

## License

Copyright (C) 2017 Inigo Surguy.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.