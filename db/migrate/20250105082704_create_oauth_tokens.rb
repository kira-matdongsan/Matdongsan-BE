class CreateOauthTokens < ActiveRecord::Migration[7.2]
  def change
    create_table :oauth_tokens do |t|
      t.references :user, null: false, foreign_key: true
      t.string :provider, null: false
      t.string :access_token, null: false
      t.string :refresh_token
      t.datetime :expires_at
      t.datetime :refresh_token_expires_at

      t.timestamps
    end
  end
end
