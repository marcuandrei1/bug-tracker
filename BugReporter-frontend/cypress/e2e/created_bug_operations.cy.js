describe('Bug operations', () => {
  it('create bug', function() {
    // login
    cy.visit('http://localhost:5173/login')
    cy.get('#root div:nth-child(2) input').type('UserTest');
    cy.get('#root div:nth-child(3) input').type('parola');
    cy.get('.btn').click();
    
    cy.url().should('eq', 'http://localhost:5173/bugs');
    
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
    
    cy.get('.btn').click();
    
    // Verify bug creation was successful
    cy.url().should('eq', 'http://localhost:5173/bugs');
    cy.get('.card').should('exist');
  });

  
  it('comment and like a bug', () => {
    // login
    cy.visit('http://localhost:5173/login')
    cy.get('#root div:nth-child(2) input').type('alexciprii');
    cy.get('#root div:nth-child(3) input').type('parola');
    cy.get('.btn').click()
    
    cy.url().should('eq', 'http://localhost:5173/bugs');
    
    // intra pe un bug
    cy.get('.card').first().click();
    
    // da like daca butonul nu este activ
    cy.get('.vote-controls > :nth-child(1)').then(($btn) => {
      if (!$btn.hasClass('vote-like-active')) {
        cy.wrap($btn).click();
      } 
    });
    
    cy.get('textarea')
      .type('Si eu am patit asta si cred ca e problema de la Digi. Nu avem ce sa facem pana nu rezolva ei asta.');
    
    cy.get('.btn').click();
    
    // Verify comment was posted
    cy.get('.comment').should('contain', 'Si eu am patit asta si cred ca e problema de la Digi. Nu avem ce sa facem pana nu rezolva ei asta.');
  });

  it('comment reply', function() {
    // login
    cy.visit('http://localhost:5173/login')
    cy.get('#root div:nth-child(2) input').type('UserTest');
    cy.get('#root div:nth-child(3) input').type('parola');
    cy.get('.btn').click();
    
    cy.url().should('eq', 'http://localhost:5173/bugs');
    
    cy.get('.card').first().click();

    cy.get('.comment-vote-controls > :nth-child(1)').then(($btn) => {
      if (!$btn.hasClass('vote-like-active')) {
        cy.wrap($btn).click();
      } 
    });
    
    // Add reply to comment
    cy.get('.comment-reply-input').type('Great point! I agree with this.');
    cy.get('.comment-reply-btn').click();
    
    // Verify reply was posted
    cy.get('.comment-reply').should('contain', 'Great point! I agree with this.');
  });
});