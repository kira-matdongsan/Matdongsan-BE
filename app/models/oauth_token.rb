class OauthToken < ApplicationRecord
  belongs_to :user

  validates :provider, presence: true
  validates :access_token, presence: true

  def expired?
    expires_at.present? && expires_at < Time.now
  end
end
