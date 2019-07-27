# Validation in the HMT  project

## This code library


`HmtMom`  creates two distinct validators:

  1. an MID `Validator`.  This ensures that the layout of the file system in an HMT editorial team's github repository follows known conventions, and that therefore a generic CITE library can be created from this repository.
  2. an `HmtValidator`. This is the top-level class for validating HMT content.  It operates on a `CiteLibrary` which must include required components of the HMT project's work.  This object can be constructed from a CEX serialization of a library.
