Feature: LoginResource

  Scenario: Registrera en ny kund
    When I try to register a new user with name "nej" and email "torsdagsapa@appan.com"
    Then The user is created


  Scenario: Registrera en ny kund med ogiltig e-postadress
    When I try to register a new user with name "Karin" and email "karin@karin"
    Then An error occurs with status 400 and message "Ogiltigt email-format"

  Scenario: Försök registrera en användare med en e-postadress som redan är registrerad
    Given An user with email "karin@karin.com" is already registered
    When I try to register a new user with name "Karin" and email "karin@karin.com"
    Then An error occurs with status 409 and message "Email already registered"