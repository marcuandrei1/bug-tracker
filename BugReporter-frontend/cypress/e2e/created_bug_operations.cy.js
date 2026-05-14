describe('Bug operations', () => {
  it('create bug', function() {
  // login
  cy.visit('http://localhost:5173/login')
  cy.get('#root div:nth-child(2) input').type('UserTest');
  cy.get('#root div:nth-child(3) input').type('test123');
  cy.get('.btn').click();
  
  // create a bug
  cy.get('.write-btn').click();
  
  cy.url()
    .should('eq', 'http://localhost:5173/create-bug')
  
  cy.get(':nth-child(2) > input')
    .type('League of Legends - Deconectare in timpul meciului');
  
  cy.get('textarea')
    .type('Am o problema cand sunt in joc mi se deconecteaza si nu merge sa dau reconnect, orice as face. Cum pot sa rezolv problema asta? Am patit asta in mai multe meciuri de ranked si am pierdut.')
  
  cy.get(':nth-child(5) > input')
    .type('lol, league of legends, lol reconnect, lol disconnecting');
  
  // cy.get('.btn').click();
  });

  
  it('comment and like a bug', () => {
    // login
    cy.visit('http://localhost:5173/bugs')
    cy.get(':nth-child(2) > input').type('alexciprii');
    cy.get(':nth-child(3) > input').type('parola123');
    cy.get('.btn').click()
    
    // intra pe un bug
    cy.get('.card').click();
    
    // da like daca butonul nu este activ
    cy.get('.vote-controls > :nth-child(1)').then(($btn) => {
      if (!$btn.hasClass('vote-like-active')) {
        cy.wrap($btn).click();
      } 
    });
    
    cy.get('textarea')
      .type('Si eu am patit asta si cred ca e problema de la Digi. Nu avem ce sa facem pana nu rezolva ei asta.');
    
    // cy.get('.btn').click();
  });

  it('comment reply', function() {
    // login
    cy.visit('http://localhost:5173/login')
    cy.get('#root div:nth-child(2) input').type('UserTest');
    cy.get('#root div:nth-child(3) input').type('test123');
    cy.get('.btn').click();
    
    cy.get('.card').click();

    cy.get('.comment-vote-controls > :nth-child(1)').then(($btn) => {
    if (!$btn.hasClass('vote-like-active')) {
        cy.wrap($btn).click();
      } 
    });
    
  });
});